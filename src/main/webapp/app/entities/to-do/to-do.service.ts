import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IToDo } from 'app/shared/model/to-do.model';

type EntityResponseType = HttpResponse<IToDo>;
type EntityArrayResponseType = HttpResponse<IToDo[]>;

@Injectable({ providedIn: 'root' })
export class ToDoService {
  public resourceUrl = SERVER_API_URL + 'api/to-dos';

  constructor(protected http: HttpClient) {}

  create(toDo: IToDo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDo);
    return this.http
      .post<IToDo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(toDo: IToDo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(toDo);
    return this.http
      .put<IToDo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IToDo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IToDo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(toDo: IToDo): IToDo {
    const copy: IToDo = Object.assign({}, toDo, {
      byDate: toDo.byDate && toDo.byDate.isValid() ? toDo.byDate.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.byDate = res.body.byDate ? moment(res.body.byDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((toDo: IToDo) => {
        toDo.byDate = toDo.byDate ? moment(toDo.byDate) : undefined;
      });
    }
    return res;
  }
}
