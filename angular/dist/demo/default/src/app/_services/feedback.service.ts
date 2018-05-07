import {Injectable} from '@angular/core';
import {Http, Response, Headers} from '@angular/http';
import 'rxjs/add/operator/map';

const header =  new Headers( {'Content-Type': 'application/json'});
const globalURL = 'myappmba-199623.appspot.com';
const user = 'john.doe@gmail.com';

// URL example: may be something likes this https://{url}/feedback/{username}/{schoolname}/{essayname}/{token}/{reviewID}

@Injectable()
export class FeedbackService {
    constructor(private http: Http) {

    }

    parseURL(url): any{
        let arr = url.split('/');
        return {'url': globalURL, 'user': arr[2], 'school': arr[3], 'essay': arr[4], 'token': arr[5], 'reviewid': arr[6], 'reviewer': arr[7]};
    }

    getData(info) {
        return this.http.get('https://' + info['url'] + '/review/users/' + info['user']
            + '/school/' + info['school'] + '/essay/' + info['essay'] +  '/draft/' + info['token'] + '/review/' + info['reviewid']).map((response: Response) => response.json());
    }

    uploadComment(info, content) {
        return this.http.post('https://' + info['url'] + '/review/users/' + info['user']
            + '/school/' + info['school'] + '/essay/' + info['essay'] +  '/draft/' + info['token'] + '/review/' + info['reviewid'], content, {headers: header});
    }

    uploadFile(info, file) {
        if (file == null || file['file'] == null || file['name'] == null) {
            return;
        }
        const formData: FormData = new FormData();
        formData.append('file', file['file'], file['name']);
        return this.http.post('https://' + info['url'] + '/review/users/' + info['user']
            + '/school/' + info['school'] + '/essay/' + info['essay'] +  '/draft/' + info['token'] + '/review/' + info['reviewid'] + '/upload', formData);
    }
}

