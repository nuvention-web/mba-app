import {Injectable} from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import 'rxjs/add/operator/map';

const header =  new Headers( {'Content-Type': 'application/json'});
const url = 'myappmba-199623.appspot.com';
const user = 'john.doe@gmail.com';

@Injectable()
export class FeedbackService {
    constructor(private http: Http) {
    }

    getData() {
        return this.http.get('https://' + url + '/feedback/users/' + user
            + '/school/Tuck/essay/Tuck_2017_1/draft/1a415c98-d64f-4e9a-a358-841ec3bed538/review/9b50465a-b18e-4636-8eb1-b78e9e67022e').map((response: Response) => response.json());

    }

    uploadComment(content) {
        return this.http.post('https://' + url + '/feedback/users/' + user
            + '/school/Tuck/essay/Tuck_2017_1/draft/1a415c98-d64f-4e9a-a358-841ec3bed538/review/9b50465a-b18e-4636-8eb1-b78e9e67022e', content, {headers: header});
    }

    uploadFile(file) {
        if (file == null || file['file'] == null || file['name'] == null) {
            return;
        }
        const formData: FormData = new FormData();
        formData.append('file', file['file'], file['name']);
        return this.http.post('https://' + url + '/feedback/users/' + user
            + '/school/Tuck/essay/Tuck_2017_1/draft/1a415c98-d64f-4e9a-a358-841ec3bed538/review/9b50465a-b18e-4636-8eb1-b78e9e67022e/upload', formData);
    }
}

