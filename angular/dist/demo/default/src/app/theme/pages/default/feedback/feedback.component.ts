import {AfterViewInit, Component, OnInit, ViewEncapsulation} from '@angular/core';
import { Ng2FileInputAction } from 'ng2-file-input';
import {FeedbackService} from "../../../../_services/feedback.service";
import { ScriptLoaderService } from "../../../../_services/script-loader.service";

@Component({
    selector: 'app-feedback',
    templateUrl: './feedback.component.html',
    styleUrls: ['./feedback.component.css'],
    encapsulation: ViewEncapsulation.None,
})
export class FeedbackComponent implements OnInit, AfterViewInit{
  content: any = undefined;
  file: {} = null;
  feedback: string;
  reviewer: string;
  submitted: any = false;
  height: any;
  constructor(private feedbackService: FeedbackService, private _script: ScriptLoaderService) {
      this.feedbackService.getData().subscribe(data => {
      this.content = data;
      this.feedback = this.content['review']['reviewComments']['comment'];
      this.reviewer = this.content['reviewer'].split(' ')[0];
    });
    this.height = window.screen.height + "px";
    console.log('start');
  }

   onAction(event) {
    console.log(event);
    if (event.action === Ng2FileInputAction.Added) {
      const f = event['file'];
      this.file = {name: f['name'], file: f};
    }
    if (event.action === Ng2FileInputAction.Removed) {
      this.file = null;
    }
  }

  onSubmit() {
    this.feedbackService.uploadComment({'comments': this.feedback}).subscribe();
    if (this.file != null && this.file['file'] != null && this.file['name'] != null) {
      this.feedbackService.uploadFile(this.file).subscribe(response => console.log(response));
    }
    this.submitted = true;
  }

    ngOnInit() {

    }
    ngAfterViewInit() {

    }



}
