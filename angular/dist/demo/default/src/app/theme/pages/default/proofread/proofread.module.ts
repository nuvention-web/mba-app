import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { LayoutModule } from '../../../layouts/layout.module';
import {FormsModule} from "@angular/forms";
import { DefaultComponent } from '../default.component';
import {ProofreadComponent, SafeHtmlPipe} from './proofread.component';

const routes: Routes = [
    {
        'path': '',
        'component': DefaultComponent,
        'children': [
            {
                'path': '',
                'component': ProofreadComponent,
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
        ProofreadComponent, SafeHtmlPipe
    ]
})
export class ProofreadModule {
}