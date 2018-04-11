import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Contact } from './contact.model';
import { ContactService } from './contact.service';
import { Follow } from '../follow/follow.model';

@Component({
    selector: 'jhi-contact-detail',
    templateUrl: './contact-detail.component.html'
})
export class ContactDetailComponent implements OnInit, OnDestroy {
    follows:Follow[];
    contact: Contact;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private contactService: ContactService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContacts();
    }

    load(id) {
        this.contactService.find(id)
            .subscribe((contactResponse: HttpResponse<Contact>) => {
                this.contact = contactResponse.body;
            });

        this.contactService.findFollowByContact(id).subscribe(
            (res: HttpResponse<Follow[]>) => {
                this.follows = res.body;
                console.log(this.follows);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );

    }

    private onError(error) {

    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInContacts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contactListModification',
            (response) => this.load(this.contact.id)
        );
    }
}
