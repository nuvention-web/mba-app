import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { LayoutModule } from '../../../layouts/layout.module';
import { DefaultComponent } from '../default.component';
import { ScheduleComponent } from './schedule.component';
import {TasksService} from "../../../../_services/tasks.service";
import {ScriptLoaderService} from "../../../../_services/script-loader.service";
import {FormsModule} from "@angular/forms";

const routes: Routes = [
    {
        'path': '',
        'component': DefaultComponent,
        'children': [
            {
                'path': '',
                'component': ScheduleComponent,
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
        ScheduleComponent,
    ], providers: [
        TasksService, ScriptLoaderService
    ]
})
export class ScheduleModule {
}