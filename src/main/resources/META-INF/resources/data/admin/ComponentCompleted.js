import Component from "./Component.js";
import DomBuilder from "./DomBuilder.js";
import {formatDate, handle} from "./Utils.js";

/**
 * @version 2024-03-18
 * @author Patrik Harag
 */
export default class ComponentCompleted extends Component {

    #node = DomBuilder.div();
    #content;
    #checkboxWithSnapshotOnly;

    refresh() {
        const params = new URLSearchParams({
            withSnapshotOnly: this.#checkboxWithSnapshotOnly.checked
        });
        fetch('/api/admin/completed?' + params, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => response.json()).then(completedList => {
            const table = DomBuilder.bootstrapTableBuilder();

            table.addRow(DomBuilder.element('tr', null, [
                DomBuilder.element('th', null, 'ID'),
                DomBuilder.element('th', null, 'User'),
                DomBuilder.element('th', null, 'Date'),
                DomBuilder.element('th', null, 'Scenario'),
                DomBuilder.element('th', null, 'Metadata'),
                DomBuilder.element('th', null, 'Snapshot'),
                DomBuilder.element('th', null, 'IP'),
                DomBuilder.element('th', null, '_'),
            ]));

            for (const completed of completedList) {
                table.addRow(DomBuilder.element('tr', null, [
                    DomBuilder.element('td', null, '' + completed.id),
                    DomBuilder.element('td', null, '' + completed.userId),
                    DomBuilder.element('td', null, DomBuilder.span(formatDate(new Date(completed.time)), {
                        style: 'white-space: nowrap;'
                    })),
                    DomBuilder.element('td', null, DomBuilder.span(completed.scenario, {
                        style: 'white-space: nowrap;'
                    })),
                    DomBuilder.element('td', null, this.#metadataColumn(completed)),
                    DomBuilder.element('td', null, this.#snapshotColumn(completed)),
                    DomBuilder.element('td', null, completed.ip),
                    DomBuilder.element('td', null, this.#actionsColumn(completed)),
                ]));
            }

            this.#content.innerHTML = '';
            this.#content.append(table.createNode());
        });
    }

    #snapshotColumn(completed) {
        if (completed.snapshotSize === 0) {
            return 'n/a';
        }

        return [
            DomBuilder.span('' + completed.snapshotSize + ' bytes ', {
                style: 'white-space: nowrap;'
            }),
            DomBuilder.element('br'),
            DomBuilder.element('a', {
                href: `/api/admin/completed/${completed.id}/snapshot.sgjs`,
                target: '_blank'
            }, 'Download'),
            ' ',
            DomBuilder.element('a', {
                href: `/admin/completed/${completed.id}/snapshot.sgjs`,
                target: '_blank'
            }, 'Play'),
            ' ',
            DomBuilder.link('X', null, () => {
                handle(false, true, fetch(`/api/admin/completed/${completed.id}/snapshot.sgjs`, {
                    method: 'DELETE',
                }), result => {
                    this.refresh();
                });
            })
        ];
    }

    #metadataColumn(completed) {
        if (completed.metadata === null || completed.metadata === undefined || completed.metadata.length === 0) {
            return 'n/a';
        }

        return [
            DomBuilder.link('Show', null, () => {
                let dialog = DomBuilder.bootstrapDialogBuilder();
                dialog.setHeaderContent('Metadata');
                dialog.setBodyContent([
                    DomBuilder.par(null, completed.metadata)
                ]);
                dialog.addCloseButton('Close');
                dialog.show(this.#node);  // TODO
            })
        ];
    }

    #actionsColumn(completed) {
        return DomBuilder.link('Delete', null, () => {
            handle(false, true, fetch('/api/admin/completed/' + completed.id, {
                method: 'DELETE',
            }), result => {
                this.refresh();
            });
        })
    }

    createNode() {
        this.#node.append(DomBuilder.div({ class: 'form-check' }, [
            this.#checkboxWithSnapshotOnly = DomBuilder.element('input', {
                class: 'form-check-input',
                type: 'checkbox'
            }),
            DomBuilder.element('label', { class: 'form-check-label' }, 'With snapshot only')
        ]));

        this.#checkboxWithSnapshotOnly.addEventListener("change", () => {
            this.refresh();
        });

        this.#node.append(this.#content = DomBuilder.div());
        return this.#node;
    }
}