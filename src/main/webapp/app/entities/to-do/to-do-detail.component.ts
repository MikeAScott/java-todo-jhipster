import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IToDo } from 'app/shared/model/to-do.model';

@Component({
  selector: 'jhi-to-do-detail',
  templateUrl: './to-do-detail.component.html'
})
export class ToDoDetailComponent implements OnInit {
  toDo: IToDo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ toDo }) => (this.toDo = toDo));
  }

  previousState(): void {
    window.history.back();
  }
}
