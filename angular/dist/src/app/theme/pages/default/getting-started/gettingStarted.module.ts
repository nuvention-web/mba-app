import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { LayoutModule } from '../../../layouts/layout.module';
import {FormsModule} from "@angular/forms";
import { DefaultComponent } from '../default.component';
import {GettingStartedComponent} from "./gettingStarted.component";

const routes: Routes = [
    {
        'path': '',
        'component': DefaultComponent,
        'children': [
            {
                'path': '',
                'component': GettingStartedComponent,
            },
        ],
    },
];

@NgModule({
    imports: [
        CommonModule, RouterModule.forChild(routes), LayoutModule, FormsModule
    ], exports: [
        RouterModule,
    ], declarations: [
        GettingStartedComponent
    ]
})
export class GettingStartedModule {
}