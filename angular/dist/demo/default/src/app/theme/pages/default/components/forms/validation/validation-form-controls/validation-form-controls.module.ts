import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { ValidationFormControlsComponent } from './validation-form-controls.component';
import { LayoutModule } from '../../../../../../layouts/layout.module';
import { DefaultComponent } from '../../../../default.component';
import { FormsModule } from '@angular/forms';
import { UsersSerivce } from '../../../../../../../_services/users.service';

const routes: Routes = [
    // {
    //     "path": "",
    //     "component": DefaultComponent,
    //     "children": [
    //         {
    //             "path": "",
    //             "component": ValidationFormControlsComponent
    //         }
    //     ]
    // }
];
@NgModule({
    imports: [
        CommonModule, RouterModule.forChild(routes), LayoutModule
        , FormsModule
    ], exports: [
        RouterModule
    ], declarations: [
        ValidationFormControlsComponent
    ], providers: [
        UsersSerivce
    ]
})
export class ValidationFormControlsModule {



}