import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Country } from '../../model/Country';
import { ActivatedRoute } from '@angular/router';
import { CountryService } from '../../services/country.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-country-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './country-detail.component.html',
  styleUrl: './country-detail.component.scss'
})
export class CountryDetailComponent implements OnInit{

  country!: Country;

  constructor(private route: ActivatedRoute, private service: CountryService, private cd: ChangeDetectorRef) {}

  ngOnInit(): void {
    const name = this.route.snapshot.paramMap.get('name');
    if (name) {
      this.service.getCountryByName(name).subscribe((data) => {
        this.country = data;
        this.cd.detectChanges();
      });
    }
  }

}
