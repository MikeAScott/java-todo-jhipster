import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TodoTestModule } from '../../../test.module';
import { ToDoComponent } from 'app/entities/to-do/to-do.component';
import { ToDoService } from 'app/entities/to-do/to-do.service';
import { ToDo } from 'app/shared/model/to-do.model';

describe('Component Tests', () => {
  describe('ToDo Management Component', () => {
    let comp: ToDoComponent;
    let fixture: ComponentFixture<ToDoComponent>;
    let service: ToDoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TodoTestModule],
        declarations: [ToDoComponent]
      })
        .overrideTemplate(ToDoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ToDoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ToDoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ToDo(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.toDos && comp.toDos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
