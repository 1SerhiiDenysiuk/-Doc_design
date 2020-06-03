import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITreatment } from 'app/shared/model/treatment.model';

@Component({
  selector: 'jhi-treatment-detail',
  templateUrl: './treatment-detail.component.html',
})
export class TreatmentDetailComponent implements OnInit {
  treatment: ITreatment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ treatment }) => (this.treatment = treatment));
  }

  previousState(): void {
    window.history.back();
  }
}
