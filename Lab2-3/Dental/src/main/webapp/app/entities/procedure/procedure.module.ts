import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DentalSharedModule } from 'app/shared/shared.module';
import { ProcedureComponent } from './procedure.component';
import { ProcedureDetailComponent } from './procedure-detail.component';
import { ProcedureUpdateComponent } from './procedure-update.component';
import { ProcedureDeleteDialogComponent } from './procedure-delete-dialog.component';
import { procedureRoute } from './procedure.route';

@NgModule({
  imports: [DentalSharedModule, RouterModule.forChild(procedureRoute)],
  declarations: [ProcedureComponent, ProcedureDetailComponent, ProcedureUpdateComponent, ProcedureDeleteDialogComponent],
  entryComponents: [ProcedureDeleteDialogComponent],
})
export class DentalProcedureModule {}
