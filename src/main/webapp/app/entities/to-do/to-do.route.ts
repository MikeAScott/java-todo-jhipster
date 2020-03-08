import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IToDo, ToDo } from 'app/shared/model/to-do.model';
import { ToDoService } from './to-do.service';
import { ToDoComponent } from './to-do.component';
import { ToDoDetailComponent } from './to-do-detail.component';
import { ToDoUpdateComponent } from './to-do-update.component';

@Injectable({ providedIn: 'root' })
export class ToDoResolve implements Resolve<IToDo> {
  constructor(private service: ToDoService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IToDo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((toDo: HttpResponse<ToDo>) => {
          if (toDo.body) {
            return of(toDo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ToDo());
  }
}

export const toDoRoute: Routes = [
  {
    path: '',
    component: ToDoComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ToDos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ToDoDetailComponent,
    resolve: {
      toDo: ToDoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ToDos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ToDoUpdateComponent,
    resolve: {
      toDo: ToDoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ToDos'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ToDoUpdateComponent,
    resolve: {
      toDo: ToDoResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ToDos'
    },
    canActivate: [UserRouteAccessService]
  }
];
