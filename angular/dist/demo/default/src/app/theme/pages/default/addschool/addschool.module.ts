import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { AddSchoolComponent } from './addschool.component';
import { LayoutModule } from '../../../layouts/layout.module';
import { SchoolsService } from '../../../../_services/schools.service';
import { IndexComponent } from '../index/index.component';
import { FormsModule } from '@angular/forms';
import { DefaultComponent } from '../default.component';

const routes: Routes = [
    {
        'path': '',
        'component': DefaultComponent,
        'children': [
            {
                'path': '',
                'component': AddSchoolComponent,
            },
        ],
    },
];


@NgModule({
    imports: [
        CommonModule, RouterModule.forChild(routes), LayoutModule
        , FormsModule
    ], exports: [
        RouterModule
    ], declarations: [
        AddSchoolComponent
    ], providers: [
        SchoolsService
    ]
})
export class AddSchoolModule {



}