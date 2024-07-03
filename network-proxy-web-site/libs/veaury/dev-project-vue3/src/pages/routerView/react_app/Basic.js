import React, { useRef } from 'react'
import { VueContainer } from 'veaury'

export default function(props) {
    const style = useRef({
        background: '#91e7fc',
        width: 500,
        margin: 'auto',
        padding: 10
    })
    return (<div style={style.current}>
        This is the React Component
        <h3>
            use the 'router-view' of 'vue-router'
        </h3>
        {/* Similar to the global component 'router-view' in Vue */}
        <VueContainer component='RouterView'/>
    </div>)
}
