import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { TodoSharedModule } from 'app/shared/shared.module';
import { TodoCoreModule } from 'app/core/core.module';
import { TodoAppRoutingModule } from './app-routing.module';
import { TodoHomeModule } from './home/home.module';
import { TodoEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    TodoSharedModule,
    TodoCoreModule,
    TodoHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    TodoEntityModule,
    TodoAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class TodoAppModule {}
