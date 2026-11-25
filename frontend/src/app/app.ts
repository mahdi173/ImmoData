import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Annonce, AnnonceService, LoadAnnoncesResult } from './services/annonce.service';
import { Item, ItemService } from './services/item.service';

@Component({
  selector: 'app-root',
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private readonly itemService = inject(ItemService);
  private readonly annonceService = inject(AnnonceService);

  protected readonly title = signal('Items');
  protected readonly items = signal<Item[]>([]);
  protected readonly annonces = signal<Annonce[]>([]);
  protected readonly error = signal<string | null>(null);
  protected readonly loadingAnnonces = signal(false);
  protected readonly loadResult = signal<LoadAnnoncesResult | null>(null);
  protected name = '';
  protected description = '';

  ngOnInit(): void {
    this.refresh();
    this.refreshAnnonces();
  }

  refresh(): void {
    this.itemService.getAll().subscribe({
      next: (items) => {
        this.items.set(items);
        this.error.set(null);
      },
      error: () => this.error.set('Unable to load items. Is the backend running?'),
    });
  }

  refreshAnnonces(): void {
    this.annonceService.getAll().subscribe({
      next: (annonces) => {
        this.annonces.set(annonces);
        this.error.set(null);
      },
      error: () => this.error.set('Unable to load annonces. Is the backend running?'),
    });
  }

  loadAnnonces(): void {
    this.loadingAnnonces.set(true);
    this.loadResult.set(null);
    this.annonceService.loadFromPillr().subscribe({
      next: (result) => {
        this.loadResult.set(result);
        this.loadingAnnonces.set(false);
        this.refreshAnnonces();
      },
      error: () => {
        this.loadingAnnonces.set(false);
        this.error.set('Unable to load annonces from Pillr.');
      },
    });
  }

  addItem(): void {
    const name = this.name.trim();
    if (!name) {
      return;
    }

    this.itemService
      .create({ name, description: this.description.trim() || undefined })
      .subscribe({
        next: () => {
          this.name = '';
          this.description = '';
          this.refresh();
        },
        error: () => this.error.set('Unable to create item.'),
      });
  }

  removeItem(id: number | undefined): void {
    if (id == null) {
      return;
    }

    this.itemService.delete(id).subscribe({
      next: () => this.refresh(),
      error: () => this.error.set('Unable to delete item.'),
    });
  }
}
