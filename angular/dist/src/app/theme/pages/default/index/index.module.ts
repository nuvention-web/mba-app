import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { IndexComponent } from './index.component';
import { LayoutModule } from '../../../layouts/layout.module';
import { DefaultComponent } from '../default.component';
import { ControlsInputGroupComponent } from '../components/forms/controls/controls-input-group/controls-input-group.component';
import { FormsModule } from '@angular/forms';
import { AddSchool } from '../../../../_services/user-add-school.service';
import { FilterSchoolsPipe, FilterSchoolsDeadlinePipe } from './school.pipe';


const routes: Routes = [
    {
        "path": "",
        "component": DefaultComponent,
        "children": [
            {
                "path": "",
                "component": IndexComponent
            }
        ]
    }
];
@NgModule({
    imports: [
        CommonModule, RouterModule.forChild(routes), LayoutModule, 
        FormsModule
    ], exports: [
        RouterModule
    ], declarations: [
        IndexComponent,ControlsInputGroupComponent, FilterSchoolsPipe, FilterSchoolsDeadlinePipe
    ], providers: [
        
    ] 
})
export class IndexModule {

}