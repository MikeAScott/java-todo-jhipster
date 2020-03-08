import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IToDo } from 'app/shared/model/to-do.model';
import { ToDoService } from './to-do.service';
import { ToDoDeleteDialogComponent } from './to-do-delete-dialog.component';

@Component({
  selector: 'jhi-to-do',
  templateUrl: './to-do.component.html'
})
export class ToDoComponent implements OnInit, OnDestroy {
  toDos?: IToDo[];
  eventSubscriber?: Subscription;

  constructor(protected toDoService: ToDoService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.toDoService.query().subscribe((res: HttpResponse<IToDo[]>) => (this.toDos = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInToDos();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IToDo): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInToDos(): void {
    this.eventSubscriber = this.eventManager.subscribe('toDoListModification', () => this.loadAll());
  }

  delete(toDo: IToDo): void {
    const modalRef = this.modalService.open(ToDoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.toDo = toDo;
  }
}
