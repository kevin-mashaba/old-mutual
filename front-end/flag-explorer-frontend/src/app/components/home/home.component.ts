import { Component, OnInit } from '@angular/core';
import { CountryService } from '../../services/country.service';
import { Country } from '../../model/Country';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit{

  countries: Country[] = [];
  loading = false;

  constructor(private countryService: CountryService, private router: Router) {}

  ngOnInit(): void {
    this.loading = true;
    this.countryService.getAllCountries().subscribe((data) => {
      this.countries = data;
      this.loading = false;
    });
  }

  goToDetail(name: string): void {
    this.router.navigate(['/country', name]);
  }
}
