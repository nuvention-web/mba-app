import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { LayoutModule } from '../../../layouts/layout.module';
import { DefaultComponent } from '../default.component';
import { SchoolComponent } from './school.component';
import { TextAreaEditorModule } from "angular-inline-editors";
import { InputEditorModule } from "angular-inline-editors";
import {FormsModule} from "@angular/forms";


const routes: Routes = [
    {
        'path': '',
        'component': DefaultComponent,
        'children': [
            {
                'path': '',
                'component': SchoolComponent,
            },
        ],
    },
];

@NgModule({
    imports: [
        CommonModule, RouterModule.forChild(routes), LayoutModule, TextAreaEditorModule.forRoot(), InputEditorModule.forRoot(), FormsModule
    ], exports: [
        RouterModule,
    ], declarations: [
        SchoolComponent,
    ],
})
export class SchoolModule {
}