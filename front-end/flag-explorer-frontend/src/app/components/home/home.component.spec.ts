import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomeComponent } from './home.component';
import { CountryService } from '../../services/country.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Country } from '../../model/Country';
import { of } from 'rxjs';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let mockService: jasmine.SpyObj<CountryService>;

  beforeEach(async () => {
    const countryServiceSpy = jasmine.createSpyObj('CountryService', ['getAllCountries']);

    await TestBed.configureTestingModule({
      imports: [HomeComponent, HttpClientTestingModule], 
      providers: [
        { provide: CountryService, useValue: countryServiceSpy }
      ]
    }).compileComponents();

    mockService = TestBed.inject(CountryService) as jasmine.SpyObj<CountryService>;
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch countries on init', () => {
    const dummyCountries: Country[] = [
      {
        name: 'Kenya',
        capital: 'Nairobi',
        population: 53000000,
        code: 'KE',
        flagUrl: 'https://flagcdn.com/ke.svg'
      }
    ];

    mockService.getAllCountries.and.returnValue(of(dummyCountries));

    fixture.detectChanges(); // triggers ngOnInit

    expect(mockService.getAllCountries).toHaveBeenCalled();
    expect(component.countries).toEqual(dummyCountries);
  });
});
