import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProcedure, Procedure } from 'app/shared/model/procedure.model';
import { ProcedureService } from './procedure.service';
import { ProcedureComponent } from './procedure.component';
import { ProcedureDetailComponent } from './procedure-detail.component';
import { ProcedureUpdateComponent } from './procedure-update.component';

@Injectable({ providedIn: 'root' })
export class ProcedureResolve implements Resolve<IProcedure> {
  constructor(private service: ProcedureService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProcedure> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((procedure: HttpResponse<Procedure>) => {
          if (procedure.body) {
            return of(procedure.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Procedure());
  }
}

export const procedureRoute: Routes = [
  {
    path: '',
    component: ProcedureComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Procedures',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProcedureDetailComponent,
    resolve: {
      procedure: ProcedureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Procedures',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProcedureUpdateComponent,
    resolve: {
      procedure: ProcedureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Procedures',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProcedureUpdateComponent,
    resolve: {
      procedure: ProcedureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Procedures',
    },
    canActivate: [UserRouteAccessService],
  },
];
