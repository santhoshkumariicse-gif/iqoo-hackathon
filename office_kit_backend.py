import http.server
import socketserver
import json
import logging
from datetime import datetime

# 18.27 Office Kit - Evidence Desk Server
# Simulates the laptop receiving deep analysis workflows from the iQOO Phone.

PORT = 8080
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(message)s')

class EvidenceDeskHandler(http.server.SimpleHTTPRequestHandler):
    def do_POST(self):
        if self.path == '/api/evidence/sync':
            content_length = int(self.headers['Content-Length'])
            post_data = self.rfile.read(content_length)
            
            try:
                payload = json.loads(post_data.decode('utf-8'))
                self.render_evidence_desk(payload)
                
                self.send_response(200)
                self.send_header('Content-type', 'application/json')
                self.end_headers()
                response = {"status": "success", "message": "Evidence synchronized to Laptop Desk"}
                self.wfile.write(json.dumps(response).encode('utf-8'))
            except json.JSONDecodeError:
                self.send_response(400)
                self.end_headers()
                logging.error("Failed to decode incoming Office Kit payload (Invalid JSON).")
            except Exception as e:
                self.send_response(500)
                self.end_headers()
                logging.error(f"Internal Server Error while parsing payload: {e}")

    def do_GET(self):
        if self.path == '/':
            self.send_response(200)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            html = """
            <html>
                <head><title>InsideMe AI - Evidence Desk</title><style>body { font-family: sans-serif; padding: 2rem; background: #111; color: #fff; }</style></head>
                <body>
                    <h1>InsideMe AI - Office Kit Evidence Desk</h1>
                    <p>Listening for incoming Android synchronization...</p>
                </body>
            </html>
            """
            self.wfile.write(html.encode('utf-8'))
        else:
            self.send_response(404)
            self.end_headers()

    def render_evidence_desk(self, payload):
        logging.info("==========================================")
        logging.info(" OFFICE KIT: EVIDENCE DESK SYNC RECEIVED")
        logging.info("==========================================")
        
        entity = payload.get('entityId', 'UNKNOWN')
        changes = payload.get('changes', [])
        evidence_chain = payload.get('evidenceChain', [])
        
        logging.info(f"ENTITY: {entity}")
        logging.info("TIMELINE:")
        if isinstance(evidence_chain, list):
            for evidence in evidence_chain:
                if isinstance(evidence, dict):
                    logging.info(f"  - [{evidence.get('date', 'Unknown')}] Condition: {evidence.get('condition', 'Unknown')}")
        else:
            logging.error("  - ERROR: Invalid evidenceChain format")
            
        logging.info("DETECTED CHANGES:")
        if isinstance(changes, list):
            for change in changes:
                if isinstance(change, dict):
                    logging.info(f"  - {change.get('attribute', 'UNK').upper()}: {change.get('previous', 'UNK')} -> {change.get('current', 'UNK')} (Confidence: {change.get('confidence', 0)})")
        else:
            logging.error("  - ERROR: Invalid changes format")
            
        logging.info("==========================================")


if __name__ == "__main__":
    with socketserver.TCPServer(("", PORT), EvidenceDeskHandler) as httpd:
        logging.info(f"InsideMe AI Office Kit Evidence Desk serving at http://localhost:{PORT}")
        logging.info("Ready to receive Red Light / Green Light transfers from the iQOO phone.")
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            logging.info("Shutting down Evidence Desk.")
