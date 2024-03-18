import Component from "./Component.js";
import DomBuilder from "./DomBuilder.js";
import {formatDate, handle} from "./Utils.js";

/**
 * @version 2024-03-18
 * @author Patrik Harag
 */
export default class ComponentUsers extends Component {

    #node = DomBuilder.div();

    refresh() {
        fetch('/api/admin/users', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(response => response.json()).then(userList => {
            const table = DomBuilder.bootstrapTableBuilder();

            table.addRow(DomBuilder.element('tr', null, [
                DomBuilder.element('th', null, 'ID'),
                DomBuilder.element('th', null, 'Role'),
                DomBuilder.element('th', null, 'Registered'),
                DomBuilder.element('th', null, 'Name'),
                DomBuilder.element('th', null, 'Email'),
                DomBuilder.element('th', null, '_'),
            ]));

            for (const user of userList) {
                table.addRow(DomBuilder.element('tr', null, [
                    DomBuilder.element('td', null, '' + user.id),
                    DomBuilder.element('td', null, user.role),
                    DomBuilder.element('td', null, DomBuilder.span(formatDate(new Date(user.timeRegistered)), {
                        style: 'white-space: nowrap;'
                    })),
                    DomBuilder.element('td', null, DomBuilder.span(user.displayName, {
                        style: 'white-space: nowrap;'
                    })),
                    DomBuilder.element('td', null, user.email),
                    DomBuilder.element('td', null, this.#actionsColumn(user)),
                ]));
            }

            this.#node.innerHTML = '';
            this.#node.append(table.createNode());
        });
    }

    #actionsColumn(user) {
        return DomBuilder.link('Delete', null, () => {
            handle(false, true, fetch('/api/admin/users/' + user.id, {
                method: 'DELETE',
            }), result => {
                this.refresh();
            });
        })
    }

    createNode() {
        return this.#node;
    }
}