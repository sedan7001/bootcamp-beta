import { Component } from '@angular/core';
import { QueryService } from 'eediom-sdk';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent {
  title = 'test4';
  query:string = '';
  result:any = [];
  runQuery:boolean = false;

  constructor(private queryService: QueryService) {
  }

  executeQuery() {
    this.queryService.query(this.query, 100, 0).then((res) => {
      this.result = res.records;
      this.runQuery = true;
    });
  }
}
