import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'patient',
        loadChildren: () => import('./patient/patient.module').then(m => m.DentalPatientModule),
      },
      {
        path: 'doctor',
        loadChildren: () => import('./doctor/doctor.module').then(m => m.DentalDoctorModule),
      },
      {
        path: 'department',
        loadChildren: () => import('./department/department.module').then(m => m.DentalDepartmentModule),
      },
      {
        path: 'treatment',
        loadChildren: () => import('./treatment/treatment.module').then(m => m.DentalTreatmentModule),
      },
      {
        path: 'procedure',
        loadChildren: () => import('./procedure/procedure.module').then(m => m.DentalProcedureModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class DentalEntityModule {}
