import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IToDo, ToDo } from 'app/shared/model/to-do.model';
import { ToDoService } from './to-do.service';

@Component({
  selector: 'jhi-to-do-update',
  templateUrl: './to-do-update.component.html'
})
export class ToDoUpdateComponent implements OnInit {
  isSaving = false;
  byDateDp: any;

  editForm = this.fb.group({
    id: [],
    description: [],
    byDate: [],
    done: []
  });

  constructor(protected toDoService: ToDoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ toDo }) => {
      this.updateForm(toDo);
    });
  }

  updateForm(toDo: IToDo): void {
    this.editForm.patchValue({
      id: toDo.id,
      description: toDo.description,
      byDate: toDo.byDate,
      done: toDo.done
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const toDo = this.createFromForm();
    if (toDo.id !== undefined) {
      this.subscribeToSaveResponse(this.toDoService.update(toDo));
    } else {
      this.subscribeToSaveResponse(this.toDoService.create(toDo));
    }
  }

  private createFromForm(): IToDo {
    return {
      ...new ToDo(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      byDate: this.editForm.get(['byDate'])!.value,
      done: this.editForm.get(['done'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IToDo>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
