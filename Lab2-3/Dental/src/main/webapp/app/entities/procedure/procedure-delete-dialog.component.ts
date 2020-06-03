import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProcedure } from 'app/shared/model/procedure.model';
import { ProcedureService } from './procedure.service';

@Component({
  templateUrl: './procedure-delete-dialog.component.html',
})
export class ProcedureDeleteDialogComponent {
  procedure?: IProcedure;

  constructor(protected procedureService: ProcedureService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.procedureService.delete(id).subscribe(() => {
      this.eventManager.broadcast('procedureListModification');
      this.activeModal.close();
    });
  }
}
