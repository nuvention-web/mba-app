import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { LayoutModule } from '../../../layouts/layout.module';
import {FormsModule} from "@angular/forms";
import { DefaultComponent } from '../default.component';
import {ProfileService} from "../../../../_services/profile.service";
import {EditEssayComponent} from "./editEssay.component";
import {NgbCollapseModule} from "@ng-bootstrap/ng-bootstrap";

const routes: Routes = [
    {
        'path': '',
        'component': DefaultComponent,
        'children': [
            {
                'path': '',
                'component': EditEssayComponent,
            },
        ],
    },
];

@NgModule({
    imports: [
        CommonModule, RouterModule.forChild(routes), LayoutModule, FormsModule, NgbCollapseModule
    ], exports: [
        RouterModule,
    ], declarations: [
        EditEssayComponent,
    ], providers: [
        ProfileService
    ]
})
export class EditEssayModule {
}