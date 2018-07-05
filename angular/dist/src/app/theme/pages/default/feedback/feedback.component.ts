import {AfterViewInit, Component, OnInit, ViewEncapsulation} from '@angular/core';
import { Ng2FileInputAction } from 'ng2-file-input';
import {FeedbackService} from "../../../../_services/feedback.service";
import { ScriptLoaderService } from "../../../../_services/script-loader.service";
import { Router} from "@angular/router";

@Component({
    selector: 'app-feedback',
    templateUrl: './feedback.component.html',
    styleUrls: ['./feedback.component.css'],
    encapsulation: ViewEncapsulation.None,
})
export class FeedbackComponent implements OnInit, AfterViewInit{
  content: any = undefined;
  file: {} = null;
  info = {} = null;
  feedback: string;
  reviewer: string;
  fileName : string;
  userFirstName: string;
  submitted: any = false;
  user:any = false;
  reviewUploadID : string;
  submitButtonDisabled = false;
  submitButtonText : string;
  height: any;
  constructor(private feedbackService: FeedbackService, private _script: ScriptLoaderService, private router: Router) {
      this.info = this.feedbackService.parseURL(this.router.url);
      this.feedbackService.getData(this.router.url).subscribe(data => {
      this.content = data;
      if (this.content['review']['reviewComments']!=null) {
          this.feedback = this.content['review']['reviewComments']['comment'];
      }
      this.reviewer = this.content['reviewer'].split(' ')[0];
      this.userFirstName = this.content['user'].split(' ')[0];
      this.reviewUploadID = this.content['review']['reviewComments']['uploadID'];
      this.fileName = this.content['review']['reviewComments']['fileName']
      });
    this.height = window.screen.height + "px";
    if(this.info["view"]=="user"){
        this.user=true
    }
    this.submitButtonText = "Submit";
    console.log('start');
  }

   onAction(event) {
    console.log(event);
    if (event.action === Ng2FileInputAction.Added) {
      const f = event['file'];
      this.file = {name: f['name'], file: f};
      console.log(f.size);
        if(f!=null && f.size>2000000) {
            this.submitButtonDisabled=true;
            this.submitButtonText="The filesize exceeds the 2MB limit. Only files upto 2MB can be uploaded. Please add a smaller file."
        }
        else{
            this.submitButtonDisabled=false;
            this.submitButtonText="Submit"
        }

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

    downloadReview(){
        this.feedbackService.downloadReviewFile()
    }



}
