import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITreatment } from 'app/shared/model/treatment.model';
import { TreatmentService } from './treatment.service';

@Component({
  templateUrl: './treatment-delete-dialog.component.html',
})
export class TreatmentDeleteDialogComponent {
  treatment?: ITreatment;

  constructor(protected treatmentService: TreatmentService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.treatmentService.delete(id).subscribe(() => {
      this.eventManager.broadcast('treatmentListModification');
      this.activeModal.close();
    });
  }
}
