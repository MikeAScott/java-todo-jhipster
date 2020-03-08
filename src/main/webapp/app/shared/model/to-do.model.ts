import { Moment } from 'moment';

export interface IToDo {
  id?: number;
  description?: string;
  byDate?: Moment;
  done?: boolean;
}

export class ToDo implements IToDo {
  constructor(public id?: number, public description?: string, public byDate?: Moment, public done?: boolean) {
    this.done = this.done || false;
  }
}
