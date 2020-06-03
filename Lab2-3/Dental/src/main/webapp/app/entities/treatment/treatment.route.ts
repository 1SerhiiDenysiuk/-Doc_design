import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITreatment, Treatment } from 'app/shared/model/treatment.model';
import { TreatmentService } from './treatment.service';
import { TreatmentComponent } from './treatment.component';
import { TreatmentDetailComponent } from './treatment-detail.component';
import { TreatmentUpdateComponent } from './treatment-update.component';

@Injectable({ providedIn: 'root' })
export class TreatmentResolve implements Resolve<ITreatment> {
  constructor(private service: TreatmentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITreatment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((treatment: HttpResponse<Treatment>) => {
          if (treatment.body) {
            return of(treatment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Treatment());
  }
}

export const treatmentRoute: Routes = [
  {
    path: '',
    component: TreatmentComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Treatments',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TreatmentDetailComponent,
    resolve: {
      treatment: TreatmentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Treatments',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TreatmentUpdateComponent,
    resolve: {
      treatment: TreatmentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Treatments',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TreatmentUpdateComponent,
    resolve: {
      treatment: TreatmentResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Treatments',
    },
    canActivate: [UserRouteAccessService],
  },
];
