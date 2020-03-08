import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TodoTestModule } from '../../../test.module';
import { ToDoDetailComponent } from 'app/entities/to-do/to-do-detail.component';
import { ToDo } from 'app/shared/model/to-do.model';

describe('Component Tests', () => {
  describe('ToDo Management Detail Component', () => {
    let comp: ToDoDetailComponent;
    let fixture: ComponentFixture<ToDoDetailComponent>;
    const route = ({ data: of({ toDo: new ToDo(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TodoTestModule],
        declarations: [ToDoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ToDoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ToDoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load toDo on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.toDo).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
