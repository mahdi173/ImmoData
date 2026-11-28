import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
  inject,
  signal,
} from '@angular/core';
import * as L from 'leaflet';
import { Annonce, AnnonceService } from './services/annonce.service';

const FRANCE_CENTER: L.LatLngExpression = [46.6, 2.4];
const FRANCE_ZOOM = 6;

@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit, AfterViewInit, OnDestroy {
  private readonly annonceService = inject(AnnonceService);

  @ViewChild('map', { static: true }) private mapEl!: ElementRef<HTMLDivElement>;

  protected readonly title = signal('Annonces');
  protected readonly annonces = signal<Annonce[]>([]);
  protected readonly mappedCount = signal(0);
  protected readonly error = signal<string | null>(null);
  protected readonly loading = signal(true);

  private map?: L.Map;
  private markers = L.layerGroup();
  private viewReady = false;

  ngOnInit(): void {
    this.refreshAnnonces();
  }

  ngAfterViewInit(): void {
    this.initMap();
    this.viewReady = true;
    this.renderMarkers(this.annonces());
  }

  ngOnDestroy(): void {
    this.map?.remove();
  }

  refreshAnnonces(): void {
    this.loading.set(true);
    this.annonceService.getAll().subscribe({
      next: (annonces) => {
        this.annonces.set(annonces);
        this.error.set(null);
        this.loading.set(false);
        if (this.viewReady) {
          this.renderMarkers(annonces);
        }
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Unable to load annonces from the database. Is the backend running?');
      },
    });
  }

  private initMap(): void {
    this.map = L.map(this.mapEl.nativeElement, {
      center: FRANCE_CENTER,
      zoom: FRANCE_ZOOM,
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
    }).addTo(this.map);

    this.markers.addTo(this.map);

    // Leaflet needs a layout pass when the container size is finalized.
    setTimeout(() => this.map?.invalidateSize(), 0);
  }

  private renderMarkers(annonces: Annonce[]): void {
    if (!this.map) {
      return;
    }

    this.markers.clearLayers();

    const withCoords = annonces.filter(
      (a): a is Annonce & { lat: number; lon: number } =>
        a.lat != null && a.lon != null && Number.isFinite(a.lat) && Number.isFinite(a.lon),
    );

    this.mappedCount.set(withCoords.length);

    const icon = L.icon({
      iconUrl: 'leaflet/marker-icon.png',
      iconRetinaUrl: 'leaflet/marker-icon-2x.png',
      shadowUrl: 'leaflet/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });

    for (const annonce of withCoords) {
      const commune = annonce.commune?.name || annonce.insee || 'Annonce';
      const price = annonce.price != null ? `${annonce.price} €` : '—';
      const surface = annonce.surface != null ? `${annonce.surface} m²` : '—';
      const type = annonce.type ?? '—';

      L.marker([annonce.lat, annonce.lon], { icon })
        .bindPopup(
          `<strong>${commune}</strong> · ${type}<br>` +
            `${price} · ${surface}<br>` +
            `<span style="color:#6b7f90">score ${annonce.score ?? '—'} · ${annonce.source ?? ''}</span>`,
        )
        .addTo(this.markers);
    }

    if (withCoords.length > 0) {
      const bounds = L.latLngBounds(withCoords.map((a) => [a.lat, a.lon] as L.LatLngTuple));
      this.map.fitBounds(bounds.pad(0.15), { maxZoom: 12 });
    } else {
      this.map.setView(FRANCE_CENTER, FRANCE_ZOOM);
    }
  }
}
