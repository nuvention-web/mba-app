import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from "../../../../auth/_guards/auth.guard";
import {FeedbackheadComponent} from "./feedbackhead.component";
import {HeaderNavComponent} from "../../../layouts/header-nav/header-nav.component";
import {FooterComponent} from "../../../layouts/footer/footer.component";
import {LayoutModule} from "../../../layouts/layout.module";

const routes: Routes = [
    {
        "path": "",
        "component": FeedbackheadComponent,
        "canActivate": [AuthGuard],
        "children": [
            {
                "path": "",
                "loadChildren": ".\/feedback.module#FeedbackModule"
            },
        ]
    }
];

@NgModule({
    declarations: [FeedbackheadComponent],
    imports: [RouterModule.forChild(routes), LayoutModule],
    exports: [RouterModule]
})
export class FeedbackheadModule { }
