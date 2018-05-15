import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { LayoutModule } from '../../../layouts/layout.module';
import {FormsModule} from "@angular/forms";
import { DefaultComponent } from '../default.component';
import { EssayComponent } from './essay.component';
import {ProfileService} from "../../../../_services/profile.service";

const routes: Routes = [
    {
        'path': '',
        'component': DefaultComponent,
        'children': [
            {
                'path': '',
                'component': EssayComponent,
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
        EssayComponent,
    ], providers: [
        ProfileService
    ]
})
export class EssayModule {
}