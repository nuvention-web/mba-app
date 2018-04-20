import { Component } from '@angular/core';
import { CommentService } from '../_service/comment.service';
import { Ng2FileInputAction } from 'ng2-file-input';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  content: any = undefined;
  file: {} = null;
  feedback: string;
  reviewer: string;
  submitted: any = false;
  height: any;
  constructor(private commentService: CommentService) {
    commentService.getData().subscribe(data => {
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
    this.commentService.uploadComment({'comments': this.feedback}).subscribe();
    if (this.file != null && this.file['file'] != null && this.file['name'] != null) {
      this.commentService.uploadFile(this.file).subscribe(response => console.log(response));
    }
    this.submitted = true;
  }



}
