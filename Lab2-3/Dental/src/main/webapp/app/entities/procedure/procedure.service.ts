import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProcedure } from 'app/shared/model/procedure.model';

type EntityResponseType = HttpResponse<IProcedure>;
type EntityArrayResponseType = HttpResponse<IProcedure[]>;

@Injectable({ providedIn: 'root' })
export class ProcedureService {
  public resourceUrl = SERVER_API_URL + 'api/procedures';

  constructor(protected http: HttpClient) {}

  create(procedure: IProcedure): Observable<EntityResponseType> {
    return this.http.post<IProcedure>(this.resourceUrl, procedure, { observe: 'response' });
  }

  update(procedure: IProcedure): Observable<EntityResponseType> {
    return this.http.put<IProcedure>(this.resourceUrl, procedure, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProcedure>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProcedure[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
