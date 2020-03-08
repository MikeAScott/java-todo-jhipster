import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TodoTestModule } from '../../../test.module';
import { ToDoUpdateComponent } from 'app/entities/to-do/to-do-update.component';
import { ToDoService } from 'app/entities/to-do/to-do.service';
import { ToDo } from 'app/shared/model/to-do.model';

describe('Component Tests', () => {
  describe('ToDo Management Update Component', () => {
    let comp: ToDoUpdateComponent;
    let fixture: ComponentFixture<ToDoUpdateComponent>;
    let service: ToDoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TodoTestModule],
        declarations: [ToDoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ToDoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ToDoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ToDoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ToDo(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ToDo();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
