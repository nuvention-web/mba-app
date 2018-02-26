import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { WidgetsBootstrapSelectComponent } from './widgets-bootstrap-select.component';
import { LayoutModule } from '../../../../../../layouts/layout.module';
import { DefaultComponent } from '../../../../default.component';
import { SchoolsService } from '../../../../../../../_services/schools.service';
import { IndexComponent } from '../../../../index/index.component';
import { FormsModule } from '@angular/forms';

const routes: Routes = [
    // {
    //     "path": "",
    //     "component": DefaultComponent,
    //     "children": [
    //         {
    //             "path": "",
    //             "component": WidgetsBootstrapSelectComponent
    //         }
    //     ]
    // }
    {
        "path": "/index",
        "component": IndexComponent
    }
];
@NgModule({
    imports: [
        CommonModule, RouterModule.forChild(routes), LayoutModule
        , FormsModule
    ], exports: [
        RouterModule
    ], declarations: [
        WidgetsBootstrapSelectComponent
    ], providers: [
        SchoolsService
    ]
})
export class WidgetsBootstrapSelectModule {



}