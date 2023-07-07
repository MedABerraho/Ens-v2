import { Component, OnInit } from '@angular/core';
import { Source } from '../model/source';
import { SourceService } from '../service/source.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-source',
  templateUrl: './source.component.html',
  styleUrls: ['./source.component.css']
})
export class SourceComponent implements OnInit {

  source: Source = new Source();
  constructor(private sourceService: SourceService, private router: Router) { }

  ngOnInit(): void {
  }

  saveSource() {
    this.sourceService.addSource(this.source).subscribe(data => {
      console.log(data);
      this.goToSourceList();
    },
      error => console.log(error));
  }

  goToSourceList() {
    this.router.navigate[('/home')];
  }

  onSubmit() {
    console.log(this.source);
    this.saveSource();
  }

}
