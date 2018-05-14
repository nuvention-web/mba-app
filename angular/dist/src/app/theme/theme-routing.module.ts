import { NgModule } from '@angular/core';
import { ThemeComponent } from './theme.component';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from "../auth/_guards/auth.guard";

const routes: Routes = [
    {
        "path": "",
        "component": ThemeComponent,
        "canActivate": [AuthGuard],
        "children": [
            {
                "path": "index.html",
                "loadChildren": ".\/pages\/default\/index\/index.module#IndexModule"
            },
            {
                "path": "404",
                "loadChildren": ".\/pages\/default\/not-found\/not-found.module#NotFoundModule"
            },
            {
                "path": "school/:school",
                "loadChildren": ".\/pages\/default\/school\/school.module#SchoolModule"
            },
            {
                "path": "school/:school/essay/:id",
                "loadChildren": ".\/pages\/default\/essay\/essay.module#EssayModule"
            },
            {
                "path": "profile/edit",
                "loadChildren": ".\/pages\/default\/profile\/profile.module#ProfileModule"
            },
            {
                "path": "school/:school/notes/:noteID/:action",
                "loadChildren": ".\/pages\/default\/notes\/notes.module#NotesModule"
            },
            {
                "path": "school/:school/recommender/:recommenderID/edit",
                "loadChildren": ".\/pages\/default\/recommender\/recommender.module#RecommenderModule"
            },
            {
                "path": "notes/:action/:school/:noteID",
                "loadChildren": ".\/pages\/default\/notes\/notes.module#NotesModule"
            },
            {
                "path": "proofread/:school/essay/:essayID/draft/:draftID",
                "loadChildren": ".\/pages\/default\/proofread\/proofread.module#ProofreadModule"
            },
            {
                "path": "information",
                "loadChildren": ".\/pages\/default\/information\/information.module#InformationModule"
            },
            {
                "path": "resume",
                "loadChildren": ".\/pages\/default\/resume\/resume.module#ResumeModule"
            },
            {
                "path": "",
                "redirectTo": "index.html",
                "pathMatch": "full"
            }
        ]
    },
    {
        "path": "feedback/:username/:schoolname/:essayname/:token/:reviewID/:view",
        "loadChildren": ".\/pages\/default\/feedback\/feedbackhead.module#FeedbackheadModule"

    },
    {
        "path": "**",
        "redirectTo": "404",
        "pathMatch": "full"
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ThemeRoutingModule { }
