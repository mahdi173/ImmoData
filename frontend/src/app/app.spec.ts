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

    const itemsReq = httpMock.expectOne(`${environment.apiUrl}/items`);
    itemsReq.flush([]);
    const annoncesReq = httpMock.expectOne(`${environment.apiUrl}/annonces`);
    annoncesReq.flush([]);

    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();

    const itemsReq = httpMock.expectOne(`${environment.apiUrl}/items`);
    itemsReq.flush([]);
    const annoncesReq = httpMock.expectOne(`${environment.apiUrl}/annonces`);
    annoncesReq.flush([]);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Items');
  });
});
