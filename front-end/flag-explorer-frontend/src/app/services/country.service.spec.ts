import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { CountryService } from './country.service';
import { HttpTestingController } from '@angular/common/http/testing';
import { Country } from '../model/Country';

  let service: CountryService;
  let httpMock: HttpTestingController;



  describe('CountryService', () => {
    let service: CountryService;
    let httpMock: HttpTestingController;
  
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [CountryService],
      });
  
      service = TestBed.inject(CountryService);
      httpMock = TestBed.inject(HttpTestingController);
    });
  
    afterEach(() => {
      httpMock.verify();
    });
  
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  
    it('should fetch all countries', () => {
      const dummyCountries: Country[] = [
        {
          name: 'Kenya',
          capital: 'Nairobi',
          population: 53000000,
          code: 'KE',
          flagUrl: 'https://flagcdn.com/ke.svg',
        },
      ];
  
      service.getAllCountries().subscribe((countries) => {
        expect(countries.length).toBe(1);
        expect(countries).toEqual(dummyCountries);
      });
  
      const req = httpMock.expectOne('http://localhost:8081/countries');
      expect(req.request.method).toBe('GET');
      req.flush(dummyCountries);
    });
  });
  
  

