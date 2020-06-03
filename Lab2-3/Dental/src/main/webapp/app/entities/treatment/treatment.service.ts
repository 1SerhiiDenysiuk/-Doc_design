import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITreatment } from 'app/shared/model/treatment.model';

type EntityResponseType = HttpResponse<ITreatment>;
type EntityArrayResponseType = HttpResponse<ITreatment[]>;

@Injectable({ providedIn: 'root' })
export class TreatmentService {
  public resourceUrl = SERVER_API_URL + 'api/treatments';

  constructor(protected http: HttpClient) {}

  create(treatment: ITreatment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(treatment);
    return this.http
      .post<ITreatment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(treatment: ITreatment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(treatment);
    return this.http
      .put<ITreatment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITreatment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITreatment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(treatment: ITreatment): ITreatment {
    const copy: ITreatment = Object.assign({}, treatment, {
      date: treatment.date && treatment.date.isValid() ? treatment.date.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((treatment: ITreatment) => {
        treatment.date = treatment.date ? moment(treatment.date) : undefined;
      });
    }
    return res;
  }
}
