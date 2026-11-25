import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Annonceur {
  id?: number;
  name: string;
  siren?: number | null;
}

export interface Commune {
  id?: number;
  name: string;
  code: string;
}

export interface Annonce {
  id?: number;
  name: string;
  source?: string;
  type?: string;
  insee?: string;
  lat?: number | null;
  lon?: number | null;
  price?: number | null;
  surface?: number | null;
  pieces?: number | null;
  priceM2?: number | null;
  dpe?: string | null;
  score?: number | null;
  rendementBrut?: number | null;
  rendementNet?: number | null;
  loyerEstime?: number | null;
  profile?: string | null;
  residenceGeree?: boolean | null;
  publishDate?: string | null;
  annonceur?: Annonceur | null;
  commune?: Commune | null;
}

export interface LoadAnnoncesResult {
  fetched: number;
  saved: number;
  pages: number;
}

@Injectable({ providedIn: 'root' })
export class AnnonceService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/annonces`;

  getAll(): Observable<Annonce[]> {
    return this.http.get<Annonce[]>(this.baseUrl);
  }

  loadFromPillr(): Observable<LoadAnnoncesResult> {
    return this.http.post<LoadAnnoncesResult>(`${this.baseUrl}/load`, {});
  }
}
