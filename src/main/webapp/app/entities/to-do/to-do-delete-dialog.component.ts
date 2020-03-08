import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IToDo } from 'app/shared/model/to-do.model';
import { ToDoService } from './to-do.service';

@Component({
  templateUrl: './to-do-delete-dialog.component.html'
})
export class ToDoDeleteDialogComponent {
  toDo?: IToDo;

  constructor(protected toDoService: ToDoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.toDoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('toDoListModification');
      this.activeModal.close();
    });
  }
}
