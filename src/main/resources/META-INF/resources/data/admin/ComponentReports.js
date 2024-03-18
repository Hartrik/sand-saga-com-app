import Component from "./Component.js";
import DomBuilder from "./DomBuilder.js";
import {formatDate, handle} from "./Utils.js";

/**
 * @version 2024-03-18
 * @author Patrik Harag
 */
export default class ComponentReports extends Component {

    #node = DomBuilder.div();

    refresh() {
        fetch('/api/admin/reports', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => response.json()).then(reports => {
            const table = DomBuilder.bootstrapTableBuilder();

            table.addRow(DomBuilder.element('tr', null, [
                DomBuilder.element('th', null, 'ID'),
                DomBuilder.element('th', null, 'User'),
                DomBuilder.element('th', null, 'Date'),
                DomBuilder.element('th', null, 'Scenario'),
                DomBuilder.element('th', null, 'Type'),
                DomBuilder.element('th', null, 'Details'),
                DomBuilder.element('th', null, 'Metadata'),
                DomBuilder.element('th', null, 'Snapshot'),
                DomBuilder.element('th', null, 'IP'),
                DomBuilder.element('th', null, '_'),
            ]));

            for (const report of reports) {
                table.addRow(DomBuilder.element('tr', null, [
                    DomBuilder.element('td', null, '' + report.id),
                    DomBuilder.element('td', null, '' + report.userId),
                    DomBuilder.element('td', null, DomBuilder.span(formatDate(new Date(report.time)), {
                        style: 'white-space: nowrap;'
                    })),
                    DomBuilder.element('td', null, DomBuilder.span(report.scenario, {
                        style: 'white-space: nowrap;'
                    })),
                    DomBuilder.element('td', null, report.location),
                    DomBuilder.element('td', null, [
                        DomBuilder.par(null, report.message)
                    ]),
                    DomBuilder.element('td', null, this.#metadataColumn(report)),
                    DomBuilder.element('td', null, this.#snapshotColumn(report)),
                    DomBuilder.element('td', null, report.ip),
                    DomBuilder.element('td', null, this.#actionsColumn(report)),
                ]));
            }

            this.#node.innerHTML = '';
            this.#node.append(table.createNode());
        })
    }

    #snapshotColumn(report) {
        if (report.snapshotSize === 0) {
            return 'n/a';
        }

        return [
            DomBuilder.span('' + report.snapshotSize + ' bytes ', {
                style: 'white-space: nowrap;'
            }),
            DomBuilder.element('br'),
            DomBuilder.element('a', {
                href: `/api/admin/reports/${report.id}/snapshot.sgjs`,
                target: '_blank'
            }, 'Download'),
            ' ',
            DomBuilder.element('a', {
                href: `/admin/reports/${report.id}/snapshot.sgjs`,
                target: '_blank'
            }, 'Play')
        ];
    }

    #metadataColumn(report) {
        if (report.metadata === null || report.metadata === undefined || report.metadata.length === 0) {
            return 'n/a';
        }

        return [
            DomBuilder.link('Show', null, () => {
                let dialog = DomBuilder.bootstrapDialogBuilder();
                dialog.setHeaderContent('Metadata');
                dialog.setBodyContent([
                    DomBuilder.par(null, report.metadata)
                ]);
                dialog.addCloseButton('Close');
                dialog.show(this.#node);  // TODO
            })
        ];
    }

    #actionsColumn(report) {
        return DomBuilder.link('Delete', null, () => {
            handle(false, true, fetch('/api/admin/reports/' + report.id, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }), result => {
                this.refresh();
            });
        })
    }

    createNode() {
        return this.#node;
    }
}