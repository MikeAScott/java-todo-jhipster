import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TodoSharedModule } from 'app/shared/shared.module';
import { ToDoComponent } from './to-do.component';
import { ToDoDetailComponent } from './to-do-detail.component';
import { ToDoUpdateComponent } from './to-do-update.component';
import { ToDoDeleteDialogComponent } from './to-do-delete-dialog.component';
import { toDoRoute } from './to-do.route';

@NgModule({
  imports: [TodoSharedModule, RouterModule.forChild(toDoRoute)],
  declarations: [ToDoComponent, ToDoDetailComponent, ToDoUpdateComponent, ToDoDeleteDialogComponent],
  entryComponents: [ToDoDeleteDialogComponent]
})
export class TodoToDoModule {}
