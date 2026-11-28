import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { App } from './app';
import { environment } from '../environments/environment';

describe('App', () => {
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();

    const annoncesReq = httpMock.expectOne(`${environment.apiUrl}/annonces`);
    annoncesReq.flush([]);

    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();

    const annoncesReq = httpMock.expectOne(`${environment.apiUrl}/annonces`);
    annoncesReq.flush([]);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Annonces');
  });

  it('should load annonces from the database only', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();

    const annoncesReq = httpMock.expectOne(`${environment.apiUrl}/annonces`);
    expect(annoncesReq.request.method).toBe('GET');
    annoncesReq.flush([
      {
        id: 1,
        name: 'lbc:1',
        source: 'lbc',
        type: 'appartement',
        insee: '75056',
        lat: 48.8566,
        lon: 2.3522,
        price: 350000,
        surface: 45,
        score: 70,
        commune: { name: 'Paris', code: '75056' },
      },
    ]);
    fixture.detectChanges();

    httpMock.expectNone(`${environment.apiUrl}/annonces/load`);
    httpMock.expectNone(`${environment.apiUrl}/items`);

    expect(fixture.componentInstance['annonces']().length).toBe(1);
    expect(fixture.componentInstance['mappedCount']()).toBe(1);
  });
});
