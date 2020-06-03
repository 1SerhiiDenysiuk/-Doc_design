import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { DentalSharedModule } from 'app/shared/shared.module';
import { DentalCoreModule } from 'app/core/core.module';
import { DentalAppRoutingModule } from './app-routing.module';
import { DentalHomeModule } from './home/home.module';
import { DentalEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    DentalSharedModule,
    DentalCoreModule,
    DentalHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    DentalEntityModule,
    DentalAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class DentalAppModule {}
