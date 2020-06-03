import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProcedure, Procedure } from 'app/shared/model/procedure.model';
import { ProcedureService } from './procedure.service';
import { ITreatment } from 'app/shared/model/treatment.model';
import { TreatmentService } from 'app/entities/treatment/treatment.service';

@Component({
  selector: 'jhi-procedure-update',
  templateUrl: './procedure-update.component.html',
})
export class ProcedureUpdateComponent implements OnInit {
  isSaving = false;
  treatments: ITreatment[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    price: [],
    treatments: [],
  });

  constructor(
    protected procedureService: ProcedureService,
    protected treatmentService: TreatmentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ procedure }) => {
      this.updateForm(procedure);

      this.treatmentService.query().subscribe((res: HttpResponse<ITreatment[]>) => (this.treatments = res.body || []));
    });
  }

  updateForm(procedure: IProcedure): void {
    this.editForm.patchValue({
      id: procedure.id,
      name: procedure.name,
      price: procedure.price,
      treatments: procedure.treatments,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const procedure = this.createFromForm();
    if (procedure.id !== undefined) {
      this.subscribeToSaveResponse(this.procedureService.update(procedure));
    } else {
      this.subscribeToSaveResponse(this.procedureService.create(procedure));
    }
  }

  private createFromForm(): IProcedure {
    return {
      ...new Procedure(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      price: this.editForm.get(['price'])!.value,
      treatments: this.editForm.get(['treatments'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcedure>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ITreatment): any {
    return item.id;
  }
}
